package com.app.messagingapplication.service;

import com.app.messagingapplication.entity.UserAccount;
import com.app.messagingapplication.repository.UserAccountRepository;
import com.app.messagingapplication.utility.custom_exceptions.InvalidDataException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class UserAccountService implements IUserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    public String createUserAccount(UserAccount userAccount) throws InvalidDataException {
        boolean valid = validateUserInformation(userAccount);
        try {
            if (valid) {
                UserAccount saveUserAccountResult = userAccountRepository.save(userAccount);
                return "User account created with nickname " + saveUserAccountResult.getNickname();
            }
            throw new InvalidDataException("Nickname cannot be empty");
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new InvalidDataException("Nickname " + userAccount.getNickname() + " already exists");
        }
    }

    private boolean validateUserInformation (UserAccount userAccount) {
        return !StringUtils.isEmpty(userAccount.getNickname());
    }

}
