package dev.vonabe.here.location;

public interface ApplicationStatusListener {

    void pause();
    void target();
    void destroy();
    boolean isActive();

}
