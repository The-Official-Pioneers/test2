package it.uniba.pioneers.testtool.data;

import android.content.res.Resources;

import java.io.IOException;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.data.model.LoggedInUser;

public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException( Resources.getSystem().getString(R.string.error_logging_in), e));
        }
    }

    public void logout() {
    }
}