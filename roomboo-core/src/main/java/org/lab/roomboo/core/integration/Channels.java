package org.lab.roomboo.core.integration;

public interface Channels {

	/**
	 * Canal para procesar los mensajes de alta de usuarios de la aplicacion.
	 */
	String SignUpInput = "sign-up-input";

	/**
	 * Canal donde se envian los mensajes de respuesta del alta de usuarios.
	 */
	String SignUpOutput = "sign-up-output";

	/**
	 * Canal donde se envian los mensajes de error durante el alta de usuarios.
	 */
	String SignUpError = "sign-up-error";

	String SignUpConfirmationAuto = "sign-up-confirmation-auto";
	String SignUpConfirmationEmail = "sign-up-confirmation-email";

	String UserTokenConfirmationInput = "user-token-confirmation-input";
	String UserTokenConfirmationOutput = "user-token-confirmation-output";
	String UserTokenConfirmationError = "user-token-confirmation-error";

	String UserNewTokenConfirmationInput = "user-new-token-confirmation-input";
	String UserNewTokenConfirmationOutput = "user-new-token-confirmation-output";

	/**
	 * Canal que procesa las notificaciones del sistema.
	 */
	String AlertInput = "alert-input";

	/**
	 * Canal encargado del envio de correo.
	 */
	String EmailOutput = "email-output";

	/**
	 * Canal que procesa las solicitudes de reserva.
	 */
	String BookingInput = "booking-input";

	/**
	 * Canal donde se recuperan los mensajes de respuesta de las reservas.
	 */
	String BookingOutput = "booking-output";

	String BookingTokenConfirmationInput = "booking-token-confirmation-input";
	String BookingTokenConfirmationOutput = "booking-token-confirmation-output";
	String BookingTokenCancelationInput = "reserve-confirmation-input";
	String BookingTokenCancelationOutput = "reserve-confirmation-output";

	String ReserveConfirmationAuto = "reserve-confirmation-auto";
	String ReserveConfirmationEmail = "reserve-confirmation-email";


}
