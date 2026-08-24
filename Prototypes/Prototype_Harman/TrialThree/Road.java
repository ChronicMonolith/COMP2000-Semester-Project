package TrialThree;

public class Road {
    @SuppressWarnings("unused")
    private int x;
    private int y;
    private int width;
    private int height;
    @SuppressWarnings("unused")
    private int laneCount;
    private int laneHeight;

    public Road(int x, int y, int width, int laneCount, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.laneCount = laneCount;
        this.laneHeight = height / laneCount;
    }

    public int getRoadY() {
        return this.y;
    }

    public int getLaneY(int laneIndex) {
        return this.y + laneIndex * laneHeight + laneHeight / 2;
    }

    public int getRoadWidth() {
        return this.width;
    }

    public int getRoadHeight() {
        return this.height;
    }
}
