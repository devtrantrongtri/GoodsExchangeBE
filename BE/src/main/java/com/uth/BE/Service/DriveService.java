package com.uth.BE.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.uth.BE.dto.res.DriveDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@org.springframework.stereotype.Service
public class DriveService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACCOUNT_KEY_PATH = getPathToGoogleCredentials();

    private static String getPathToGoogleCredentials() {
        String curDir = System.getProperty("user.dir");
        Path path = Paths.get(curDir, "drive.json");
        return path.toString();
    }

    public DriveDTO uploadImgToDrive(InputStream inputStream, String fileName) throws GeneralSecurityException, IOException {
        DriveDTO dto = new DriveDTO();
        try {
            String folderId = "1Ebk8eMWtLBYRZJzulCS2zW60Pp6FFwrR";
            Drive drive = createDriveService();

            com.google.api.services.drive.model.File fileDrive = new com.google.api.services.drive.model.File();
            fileDrive.setName(fileName);
            fileDrive.setParents(Collections.singletonList(folderId));

            InputStreamContent mediaContent = new InputStreamContent("image/jpeg", inputStream);

            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileDrive, mediaContent)
                    .setFields("id").execute();

            String imageUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
            System.out.println("Image url: " + imageUrl);

            dto.setStatus(200);
            dto.setMessage("Successfully uploaded image");
            dto.setUrl(imageUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            dto.setStatus(500);
            dto.setMessage(e.getMessage());
        }
        return dto;
    }

    private Drive createDriveService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACCOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName("GoodsExchange").build();
    }
}
