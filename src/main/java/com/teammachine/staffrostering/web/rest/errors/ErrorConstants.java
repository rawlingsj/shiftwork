package com.teammachine.staffrostering.web.rest.errors;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_ACCESS_DENIED = "error.accessDenied";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
    public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";

    public static final String ERR_IO = "error.io";

 // Custom
    public static final String ERR_NO_SUCH_SHIFT_DATE = "noSuchShiftDate";
    public static final String ERR_NO_SUCH_EMPLOYEE = "noSuchEmployee";
    public static final String ERR_NO_SUCH_PLANNING_JOB = "noSuchPlanningJob";
    public static final String ERR_UNABLE_TO_RUN_PLANNING_JOB = "unableToRunPlanningJob";

    private ErrorConstants() {
    }

}
