package finalmission.external;

public record HolidayResponse(
        String locdate,
        String dateKind,
        String isHoliday,
        String dateName
) {

    public boolean isSameDate(String locdate) {
        return locdate.equals(this.locdate);
    }
}
