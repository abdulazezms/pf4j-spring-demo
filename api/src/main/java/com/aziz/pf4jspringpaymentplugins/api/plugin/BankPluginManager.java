package com.aziz.pf4jspringpaymentplugins.api.plugin;

import com.aziz.pf4jspringpaymentplugins.api.exception.*;
import com.aziz.pf4jspringpaymentplugins.api.exception.UnsupportedPluginFileException;
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
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankPluginManager {

    public static final Path pluginsRootLocation = Paths.get("api/target/plugins");

    private static final Map<String, String> bankIdToPluginId = new HashMap<>();

    private final SpringPluginManager springPluginManager;


    public void addBankPlugin(String bankId, MultipartFile jarFile) {
        if(bankIdToPluginId.containsKey(bankId)) {
            throw new BankPluginAlreadyExistsException("A plugin with the mapped bank ID already exists");
        }
        String pluginFilePath = createBankPlugin(bankId, jarFile);
        try {
            String pluginId = springPluginManager.loadPlugin(Path.of(pluginFilePath));
            bankIdToPluginId.put(bankId, pluginId);
            springPluginManager.startPlugin(pluginId);
        } catch (PluginAlreadyLoadedException ex) {
            throw new BankPluginAlreadyExistsException("""
                    A plugin with the same plugin ID already exists.
                    Validate that plugin.properties.plugin.id in your plugin is unique
                    """);
        }
    }

    private String createBankPlugin(String bankId, MultipartFile jarFile) {
        String fileName = jarFile.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".jar")) { //inspect the jar to validate...
            throw new UnsupportedPluginFileException("Plugin file must be a jar file");
        }

        try {
            Files.createDirectories(pluginsRootLocation);
            fileName = StringUtils.cleanPath(Objects.requireNonNull(fileName));
            Path targetLocation = pluginsRootLocation.resolve(fileName);
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
        String bankPluginId = getPluginId(bankId)
                .orElseThrow(() -> new UnsupportedBankException("Bank is not supported"));

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

    private Optional<String> getPluginId(String bankId) {
        String pluginId = bankIdToPluginId.getOrDefault(bankId, null);
        return pluginId == null? Optional.empty() : Optional.of(pluginId);
    }
}
