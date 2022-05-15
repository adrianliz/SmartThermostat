package adrianliz.apps.backoffice.backend;

import adrianliz.apps.ApplicationTestCase;
import org.springframework.transaction.annotation.Transactional;

@Transactional("backoffice-transaction_manager")
public abstract class BackofficeApplicationTestCase extends ApplicationTestCase {}
