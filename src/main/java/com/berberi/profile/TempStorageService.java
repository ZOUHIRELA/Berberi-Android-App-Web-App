package com.berberi.profile;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TempStorageService {

    private final Map<Integer, TempProfileUpdate> storage = new HashMap<>();

    public void storeTemporaryProfileUpdate(Integer userId, String email, String fullName, String phoneNumber, MultipartFile photo) throws IOException {
        TempProfileUpdate profileUpdate = new TempProfileUpdate(email, fullName, phoneNumber, photo);
        storage.put(userId, profileUpdate);
    }

    public TempProfileUpdate getTemporaryProfileUpdate(Integer userId) {
        return storage.get(userId);
    }

    public void clearTemporaryProfileUpdate(Integer userId) {
        storage.remove(userId);
    }
}
