package kr.ac.sejong.web.env;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import kr.ac.sejong.util.CommonUtil;
import kr.ac.sejong.web.exception.EnvException;
import kr.ac.sejong.web.exception.FileNotFoundExceptoin;

public class YamlParser {

    private Path file;
    private Map<String, String> root_env = new HashMap<>();
    private Object httpManager;

    private final Set<String> IGNORE_LIST = new HashSet<>();
    private final String URL_KEY = "URL";

    public YamlParser(String file) throws FileNotFoundExceptoin {
        this(Path.of(file));
    }

    public YamlParser(Path file) throws FileNotFoundExceptoin {
        this.IGNORE_LIST.add(URL_KEY);
        this.file = file;
        read();
    }

    public Map<String, String> getRootEnv() {
        return this.root_env;
    }

    public Object getHttpManagement() {
        return this.httpManager;
    }

    private void read() {
        File file = this.file.toFile();
        Yaml yaml = new Yaml();
        try {
            Object data = yaml.load(new FileInputStream(file));
            readRootEnv(data);
            makeHttpManager(data);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundExceptoin(e.getMessage());
        } catch (EnvException e) {
            throw e;
        } catch (ClassCastException e) {            
            throw new EnvException("Invalid format in file");
        } catch (Exception e) {
            throw new EnvException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void readRootEnv(Object data) {
        Map<String, Object> root_data = (Map<String, Object>) data;
        for (Entry<String, Object> root_entry : root_data.entrySet()) {
            if (this.IGNORE_LIST.contains(root_entry.getKey()))
                continue;
            if(root_entry.getValue() == null )
                continue;
            String value = root_entry.getValue().toString();
            if(value.isBlank())
                continue;
            this.root_env.put(root_entry.getKey(), value);
        }
    }

    @SuppressWarnings("unchecked")
    private Object makeHttpManager(Object data) {
        LinkedHashMap<String, Object> root_info = (LinkedHashMap<String, Object>) data;
        LinkedHashMap<String, Object> url_info = (LinkedHashMap<String, Object>) root_info.get(URL_KEY);
        return makeHttpManager(url_info, "/");
    }

    @SuppressWarnings("unchecked")
    private Object makeHttpManager(LinkedHashMap<String, Object> urlData, String urlPrefix) {
        Map<String, Object> management = new HashMap<>();

        for (Entry<String, Object> eachUrlEnv : urlData.entrySet()) {
            String url = eachUrlEnv.getKey();
            if(!url.startsWith("/"))
                continue;
            LinkedHashMap<String, Object> info = (LinkedHashMap<String, Object>) eachUrlEnv.getValue();
            String type = (String) info.get("type");
            switch (type) {
                case "url":
                    management.put(url, makeHttpManager(info, urlPrefix + url));
                    break;
                case "file":
                    management.put(url, makeFileHandler(info));
                    break;
                case "import":
                    management.put(url, makeImportManager(info));
                    break;
                default:
                    throw new EnvException("Unknown type in the setting file - " + type);
            }
        }
        return management;
    }

    private Object makeFileHandler(LinkedHashMap<String, Object> info) {
        String path = (String) info.get("path");
        if (CommonUtil.isEmpty(path))
            throw new EnvException("file type need path");
        return null;
    }

    private Object makeImportManager(LinkedHashMap<String, Object> info) {
        String path = (String) info.get("path");
        if (CommonUtil.isEmpty(path))
            throw new EnvException();


        return null;
    }
}
