package CanvasTestObjects.CanvasShapes;

public class TestCanvasCord{

    Double[] cord=new Double[2];
    TestCanvasCord(){
        cord[0]=null;
        cord[1]=null;        
    }
    TestCanvasCord(double x, double y){
        cord[0]=x;
        cord[1]=y;
    }

    public void setX(double x){
        cord[0]=x;
    }
    public void setY(double y){
        cord[1]=y;
    }
    public double getX(){
        return cord[0];
    }
    public double getY(){
        return cord[1];
    }

}