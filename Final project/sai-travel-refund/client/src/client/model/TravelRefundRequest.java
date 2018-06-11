package client.model;

/**
 *
 * This class stores all information about a request that a client submits to
 * get a travel refundation request.
 *
 */
public class TravelRefundRequest {

    private Address origin;
    private Address destination;
    private String teacher;
    private String student;
    private double costs;
    private ClientTravelMode mode;

    public TravelRefundRequest() {
        super();
        setCosts(0);
        setOrigin(null);
        setDestination(null);
        setStudent(null);
        setTeacher(null);
        setMode(ClientTravelMode.CAR);
    }

    public TravelRefundRequest(String teacher, String student, Address origin, Address destination) {
        super();
        setCosts(0);
        setOrigin(origin);
        setDestination(destination);
        setStudent(student);
        setTeacher(teacher);
        setMode(ClientTravelMode.CAR);
    }

    public TravelRefundRequest(String teacher, String student, Address origin, Address destination, double costs) {
        super();
        setCosts(0);
        setOrigin(origin);
        setDestination(destination);
        setStudent(student);
        setTeacher(teacher);
        setCosts(costs);
    }

    public Address getOrigin() {
        return origin;
    }

    public void setOrigin(Address origin) {
        this.origin = origin;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
        if (costs <= 0) {
            mode = ClientTravelMode.CAR;
        }

    }

    @Override
    public String toString() {
        return teacher + "-" + student + "-" + origin + "-" + destination +"-" +mode+"-"+costs;
    }

    public ClientTravelMode getMode() {
        return mode;
    }

    public void setMode(ClientTravelMode mode) {
        if (mode == ClientTravelMode.CAR) {
            this.costs = 0;
        }
        this.mode = mode;
    }
}
