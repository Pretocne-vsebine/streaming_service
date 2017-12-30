package com.rso.streaming.logic;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.*;
import org.apache.logging.log4j.ThreadContext;
import sun.misc.IOUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class ClipBean {
    private static final Logger LOG = LogManager.getLogger(ClipBean.class.getName());

    @PostConstruct
    private void init() {
        HashMap settings = new HashMap();
        settings.put("environment", EeConfig.getInstance().getEnv().getName());
        settings.put("serviceName", EeConfig.getInstance().getName());
        settings.put("applicationVersion", EeConfig.getInstance().getVersion());
        settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());
        settings.put("uniqueRequestId", UUID.randomUUID().toString());

        ThreadContext.putAll(settings);
    }

    public boolean addFile(InputStream file, long ID) {
        try {
            File targetFile = new File("/data/" + ID + ".mp3");

            java.nio.file.Files.copy(
                    file,
                    targetFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            file.close();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

    public InputStream getFile(long ID) {
        try {
            InputStream file = new FileInputStream(new File("/data/" + ID + ".mp3"));
            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
