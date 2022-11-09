package net.jlxxw.robot.filter.core.vo.base;

/**
 * page result
 * @author chunyang.leng
 * @date 2022-03-24 3:27 PM
 */
public enum ResultCode implements ResultCodeInterface{

    /**
     * success
     */
    SUCCESS(true, "success"),

    /**
     * failed
     */
    SYSTEM_ERROR(false, "system error ");

    private final boolean code;
    private final String message;

    ResultCode(boolean code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }


}
