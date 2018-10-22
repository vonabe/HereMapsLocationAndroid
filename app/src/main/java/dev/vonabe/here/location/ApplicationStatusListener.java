package dev.vonabe.here.location;

public interface ApplicationStatusListener {

    void pause();
    void target();
    void destroy();
    void activeDetect();
    void deactiveDetect();
    void detectTarget();
    boolean isActive();

}
