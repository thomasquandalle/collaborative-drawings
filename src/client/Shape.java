package client;

public enum Shape {
    CIRCLE ("circleShape"),
    SQUARE ("squareShape");

    private final String fxId;

    Shape(String fxId){
        this.fxId = fxId;
    }


    public static Shape getShapeFromId(String id){
        switch(id){
            case "circleShape":
                return Shape.CIRCLE;
            case "squareShape":
                return Shape.SQUARE;
        }
        return null;
    }
}