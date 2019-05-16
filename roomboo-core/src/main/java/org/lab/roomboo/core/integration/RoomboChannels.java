package org.lab.roomboo.core.integration;

public class RoomboChannels {

	/**
	 * Canal para procesar los mensajes de alta de usuarios de la aplicacion.
	 */
	public static final String SIGN_UP_IN = "sign-up-input";

	/**
	 * Canal donde se envian los mensajes de respuesta del alta de usuarios.
	 */
	public static final String SIGN_UP_OUT = "sign-up-output";

	/**
	 * Canal donde se envian los mensajes de error durante el alta de usuarios.
	 */
	public static final String SIGN_UP_ERR = "sign-up-error";

	public static final String SIGN_UP_CONFIRMATION_AUTO = "sign-up-confirmation-auto";
	public static final String SIGN_UP_CONFIRMATION_EMAIL = "sign-up-confirmation-email";

	public static final String USER_TOKEN_CONFIRMATION_IN = "user-token-confirmation-input";
	public static final String USER_TOKEN_CONFIRMATION_OUT = "user-token-confirmation-output";
	public static final String USER_TOKEN_CONFIRMATION_ERR = "user-token-confirmation-error";

	public static final String USER_NEW_TOKEN_CONFIRMATION_IN = "user-new-token-confirmation-input";
	public static final String USER_NEW_TOKEN_CONFIRMATION_OUT = "user-new-token-confirmation-output";

	/**
	 * Canal que procesa las notificaciones del sistema.
	 */
	public static final String ALERT_IN = "alert-input";

	/**
	 * Canal encargado del envio de correo.
	 */
	public static final String EMAIL_OUT = "email-output";

	/**
	 * Canal que procesa las solicitudes de reserva.
	 */
	public static final String BOOKING_IN = "booking-input";

	/**
	 * Canal donde se recuperan los mensajes de respuesta de las reservas.
	 */
	public static final String BOOKING_OUT = "booking-output";

	public static final String BOOKING_TOKEN_CONFIRMATION_IN = "booking-token-confirmation-input";
	public static final String BOOKING_TOKEN_CONFIRMATION_OUT = "booking-token-confirmation-output";
	public static final String BOOKING_TOKEN_CANCELLATION_IN = "reserve-confirmation-input";
	public static final String BOOKING_TOKEN_CANCELLATION_OUT = "reserve-confirmation-output";

	public static final String RESERVE_CONFIRMATION_AUTO = "reserve-confirmation-auto";
	public static final String RESERVE_CONFIRMATION_EMAIL = "reserve-confirmation-email";

	private RoomboChannels() {
	}

}
