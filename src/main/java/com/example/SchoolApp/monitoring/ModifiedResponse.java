package com.example.SchoolApp.monitoring;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class ModifiedResponse extends HttpServletResponseWrapper {

    public ModifiedResponse(HttpServletResponse response) {
        super(response);
    }
    @Override
    public void setStatus(int sc) {

    }
}
