package controllers;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class JarFileController {
    private final AtomicLong counter = new AtomicLong();
    private final static String PLUGINS_FOLDER = "plugins";
    private final static String TEMPORARY_FOLDER = "temp";
    private final static String P2_PROVISION_BAT_NAME = "p2Provision.bat";

    @Autowired
    private Environment env;

    Logger logger = LoggerFactory.getLogger(JarFileController.class);

    @RequestMapping(value = "/uploadJars", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    @ResponseBody
    @Scope("session")
    public ResponseEntity getJarFiles(MultipartHttpServletRequest multipartRequest) throws IOException, InterruptedException {
        Map<String, MultipartFile> files = multipartRequest.getFileMap();

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Attempt to upload jars is made. Logged as " + userName);

        String p2Location = env.getProperty("p2.directory");
        String applicationLocation = env.getProperty("session.working.directory");
        logger.info("Application folder location is {}", applicationLocation);
        logger.info("P2 location found {}", p2Location);

        Path userSessionDirectory = Paths.get(applicationLocation, userName);
        //this can fail on second repository creation, since user session directory is the same
        userSessionDirectory.toFile().mkdirs();

        Path userSessionTempDir = Paths.get(userSessionDirectory.toString(), (TEMPORARY_FOLDER + System.currentTimeMillis()));
        Path userSessionTempPluginsFolder = Paths.get(userSessionTempDir.toString(), PLUGINS_FOLDER);
        if (!(createDirecotries(userSessionTempDir) && createDirecotries(userSessionTempPluginsFolder))) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Path> localPathsToTransferredFiles = new ArrayList<>();
        for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            Path transferedFileLocation = Paths.get(userSessionTempPluginsFolder.toString(), entry.getValue().getOriginalFilename());
            entry.getValue().transferTo(transferedFileLocation);
            logger.info("Created file: {}", transferedFileLocation);
            localPathsToTransferredFiles.add(transferedFileLocation);
        }

        Path repositoryPath = Paths.get(userSessionDirectory.toString(), Long.toString(counter.getAndIncrement()));
        Path provisionExecutable = Paths.get(p2Location, P2_PROVISION_BAT_NAME);

        provisionBundlesFromTempFolder(provisionExecutable, userSessionTempDir, repositoryPath, Paths.get(p2Location));

        FileUtils.deleteDirectory(userSessionTempDir.toFile());

        multipartRequest.getSession().setAttribute("directory", userSessionDirectory);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    private void provisionBundlesFromTempFolder(Path executable, Path source, Path destination, Path executableFolder)
            throws IOException, InterruptedException {
        List<String> args = new ArrayList<String>();
        args.add(executable.toString()); // command name
        args.add(source.toString()); // -source parameter
        args.add(destination.toUri().toString()); // url of repository

        logger.info("Invoking p2 provision with arguments");
        args.forEach(it -> {
            logger.info(it);
        });

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(executableFolder.toFile());
        Process p = pb.start();
        p.waitFor(10, TimeUnit.SECONDS);
    }

    private boolean createDirecotries(Path direcotry) {
        logger.debug("Creating directory {}", direcotry);
        if (!direcotry.toFile().mkdirs()) {
            logger.error("Failed to create direcotry {}", direcotry);
            return false;
        }
        return true;
    }
}
