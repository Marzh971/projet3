package fr.spam.backbookhub.shared.models;

import java.util.function.Function;

public class ServiceResponse<T> {

    // Attributes
    private String code;
    private String message;
    private T data;

    // Constructors
    public ServiceResponse() {
    }
    public ServiceResponse(String code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    // Getters & Setters
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    public <R> ServiceResponse<R> map(Function<T, R> mapper) {
        ServiceResponse<R> mapped = new ServiceResponse<>();
        mapped.setCode(this.code);
        mapped.setMessage(this.message);
        mapped.setData(mapper.apply(this.data));
        return mapped;
    }
}