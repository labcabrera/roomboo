package org.lab.roomboo.core.integration;

public interface RoombooIntegration {

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

		/**
		 * Canal que procesa las notificaciones del sistema.
		 */
		String AlertInput = "alert-input";

		/**
		 * Canal encargado del envio de correo.
		 */
		String EmailOutput = "email-output";

	}

}
