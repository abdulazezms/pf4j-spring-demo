package com.aziz.pf4jspringpaymentplugins.api.plugin;

import com.aziz.pf4jspringpaymentplugins.api.config.AppProperties;
import com.aziz.pf4jspringpaymentplugins.api.entity.BankPlugin;
import com.aziz.pf4jspringpaymentplugins.api.exception.BankPluginAlreadyExistsException;
import com.aziz.pf4jspringpaymentplugins.api.exception.ExtensionNotFoundException;
import com.aziz.pf4jspringpaymentplugins.api.exception.MoreThanOneExtensionFound;
import com.aziz.pf4jspringpaymentplugins.api.exception.PluginJarProcessingException;
import com.aziz.pf4jspringpaymentplugins.api.exception.UnsupportedBankException;
import com.aziz.pf4jspringpaymentplugins.api.exception.UnsupportedPluginFileException;
import com.aziz.pf4jspringpaymentplugins.api.repository.BankPluginRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginAlreadyLoadedException;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankPluginManager {

    private final SpringPluginManager springPluginManager;

    private final BankPluginRepository bankPluginRepository;

    private final AppProperties appProperties;


    public void addBankPlugin(String bankId, MultipartFile jarFile) {
        if(bankPluginRepository.existsByBankId(bankId)) {
            throw new BankPluginAlreadyExistsException("A plugin with the mapped bank ID already exists");
        }
        String pluginFilePath = persistJar(jarFile);
        try {
            String pluginId = springPluginManager.loadPlugin(Path.of(pluginFilePath));
            BankPlugin bankPlugin = new BankPlugin();
            bankPlugin.setPluginId(pluginId);
            bankPlugin.setBankId(bankId);
            bankPluginRepository.save(bankPlugin);
            springPluginManager.startPlugin(pluginId);
        } catch (PluginAlreadyLoadedException ex) {
            throw new BankPluginAlreadyExistsException("""
                    A plugin with the same plugin ID already exists.
                    Validate that plugin.properties.plugin.id in your plugin is unique
                    """);
        }
    }

    private String persistJar(MultipartFile jarFile) {
        String fileName = jarFile.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".jar")) { //inspect the jar to validate...
            throw new UnsupportedPluginFileException("Plugin file must be a jar file");
        }

        try {
            var pluginsPath = Path.of(appProperties.getPluginsPath());
            Files.createDirectories(pluginsPath);
            fileName = StringUtils.cleanPath(Objects.requireNonNull(fileName));
            Path targetLocation = pluginsPath.resolve(fileName);
            if(Files.exists(targetLocation)) {
                throw new BankPluginAlreadyExistsException("Bank plugin already exists");
            }
            jarFile.transferTo(targetLocation);
            return targetLocation.toString();
        } catch (BankPluginAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new PluginJarProcessingException("Failed to process plugin jar file");
        }
    }

    public <T> T getBankPluginImplementation(String bankId, Class<T> clazz) {
        log.debug("Searching for bank plugin implementation for: {}", clazz.getSimpleName());
        BankPlugin bankPlugin = getPluginId(bankId)
                .orElseThrow(() -> new UnsupportedBankException("Bank is not supported"));
        String bankPluginId = bankPlugin.getPluginId();
        List<T> extensions = springPluginManager.getExtensions(clazz, bankPluginId);
        if(extensions.isEmpty()) {
            log.debug("No implementation found in bank's plugin: {} for: {}", bankPluginId, clazz.getSimpleName());
            throw new ExtensionNotFoundException("No strategy found to perform this operation");
        }
        if(extensions.size() > 1) {
            log.warn("More than one implementation found in bank's plugin: {} for: {}", bankPluginId, clazz.getSimpleName());
            throw new MoreThanOneExtensionFound("More than one strategy found to perform operation");
        }
        return extensions.getFirst();
    }

    @PreDestroy
    public void cleanup() {
        springPluginManager.stopPlugins();
    }

    private Optional<BankPlugin> getPluginId(String bankId) {
        return bankPluginRepository.findByBankId(bankId);
    }
}
