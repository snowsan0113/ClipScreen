package snowsan0113.clipscreen.util;

public record Location(double x, double y, double z) {

    @Override
    public String toString() {
        return String.format("Location{x=%f,y=%f,z=%f}", x, y, z);
    }
}
