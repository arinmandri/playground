package xyz.arinmandri.playground.core;

/**
 * 로그인 되는
 * 회원, 관리자
 */
public interface Loginable {
	public static final int OWNER_TYPE_ADMEMBER = 0;
	public static final int OWNER_TYPE_MEMBER = 1;

    public Long getId();
    public int getLoginableType();
}
