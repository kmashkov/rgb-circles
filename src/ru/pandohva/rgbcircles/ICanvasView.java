package ru.pandohva.rgbcircles;

public interface ICanvasView {
    void drawCircle(SimpleCircle circle);
    
    void redraw();
    
    void showMessage(String text);
}
