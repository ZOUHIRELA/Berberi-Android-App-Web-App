package com.berberi.profile;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class TempStorageService {

    private final Map<Integer, TempProfileUpdate> storage = new HashMap<>();

    public void storeTemporaryProfileUpdate(Integer userId, String email, String fullName, String phoneNumber, MultipartFile photo) {
        storage.put(userId, new TempProfileUpdate(email, fullName, phoneNumber, photo));
    }

    public TempProfileUpdate getTemporaryProfileUpdate(Integer userId) {
        return storage.get(userId);
    }

    public void clearTemporaryProfileUpdate(Integer userId) {
        storage.remove(userId);
    }
}
