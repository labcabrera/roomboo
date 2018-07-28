package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.lab.roomboo.domain.model.AppUser.ValidationScope.Register;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "appUser")
@Valid
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

	@NotNull(message = "validation.AppUser.id.required", groups = { Update.class })
	private String id;

	@NotBlank(message = "validation.AppUser.email.required", groups = { Register.class, Update.class })
	@Email(message = "validation.AppUser.email.invalidFormat", groups = { Register.class, Update.class })
	private String email;

	private String displayName;

	@Null(message = "validation.AppUser.created.defined", groups = { Register.class, Update.class })
	private LocalDateTime created;

	@Null(message = "validation.AppUser.activation.defined", groups = { Register.class })
	private LocalDateTime activation;

	@Null(message = "validation.AppUser.expiration.defined", groups = { Register.class })
	private LocalDateTime expiration;

	@Null(message = "validation.AppUser.locked.defined", groups = { Register.class })
	private LocalDateTime locked;

	public interface ValidationScope {
		public interface Register {
		}

		public interface Update {
		}
	}
}
