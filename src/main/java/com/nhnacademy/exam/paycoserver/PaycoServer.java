package com.nhnacademy.exam.paycoserver;

import com.nhnacademy.exam.car.User;
import java.util.Objects;

public class PaycoServer {
    public boolean checkIsUserValid(User user) {
        return Objects.equals(user.getId(), getCodeFromServer());
    }

    public Object getCodeFromServer() {
        return null;
    }
}
