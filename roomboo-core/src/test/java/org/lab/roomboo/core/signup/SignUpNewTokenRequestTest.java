package org.lab.roomboo.core.signup;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.roomboo.core.RoombooCoreConfig;
import org.lab.roomboo.core.model.SignUpRequest;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.lab.roomboo.domain.repository.UserConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoombooCoreConfig.class)
public class SignUpNewTokenRequestTest extends SignUpTest {

	@Autowired
	private UserConfirmationTokenRepository tokenRepository;

	@Test
	public void test() {
		String companyId = "roombooCompanyDemo01";

		Optional<Company> company = companyRepository.findById(companyId);
		Assume.assumeTrue(company.isPresent());
		Assert.assertEquals(Company.SignUpActivationMode.EMAIL, company.get().getSignUpActivationMode());

		SignUpRequest request = getValidSignUpRequest(companyId);

		AppUser user = gateway.signUp(request);
		Assert.assertNull(user.getActivation());

		awaitTaskTermination();

		UserConfirmationToken firstToken = tokenRepository.findByUserId(user.getId()).get();
		Assert.assertNotNull(firstToken);

		gateway.confirmationTokenRequest(user.getId());

		awaitTaskTermination();

		UserConfirmationToken secondToken = tokenRepository.findByUserId(user.getId()).get();
		Assert.assertNotNull(secondToken);

		Assert.assertFalse(tokenRepository.findById(firstToken.getId()).isPresent());
		Assert.assertNotEquals(firstToken.getToken(), secondToken.getToken());

	}

}
