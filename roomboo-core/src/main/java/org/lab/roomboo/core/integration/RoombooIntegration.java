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

		/**
		 * Canal de entrada de los mensajes para la activacion de usuarios. Podemos tener diferentes estrategias de
		 * activacion (por ejemplo auto-activacion, confirmacion via correo, etc).
		 */
		String PrepareUserActivation = "prepare-user-activation";

		/**
		 * Canal que procesa las notificaciones del sistema.
		 */
		String AlertInput = "alert-input";

	}

}
