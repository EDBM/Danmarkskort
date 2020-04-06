package BFST20Project;

public class BoundingBox {

    private double minX, maxX, minY, maxY;

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }



    public BoundingBox calculateBoundingBox(float[][] coordinates) {

        BoundingBox boundingBox = new BoundingBox();

        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;

        for (int i = 0; i < coordinates.length; i++) {
            float x = coordinates[i][0];
            float y = coordinates[i][1];

            minX = Math.min(minX, x);
            minY = Math.min(minX, y);

            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        boundingBox.setMaxX(maxX);
        boundingBox.setMaxY(maxY);

        boundingBox.setMinX(minX);
        boundingBox.setMinY(minY);

        return boundingBox;
    }


}
