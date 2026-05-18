package internal

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.main.TestCaseMain


/**
 * This class is generated automatically by Katalon Studio and should not be modified or deleted.
 */
public class GlobalVariable {
     
    /**
     * <p>Profile default : Frontend base URL.</p>
     */
    public static Object FRONTEND_URL
     
    /**
     * <p>Profile default : Default wait timeout in seconds.</p>
     */
    public static Object DEFAULT_TIMEOUT
     
    /**
     * <p>Profile default : Password used for generated test accounts.</p>
     */
    public static Object TEST_PASSWORD
     
    /**
     * <p>Profile default : Delay in seconds after important UI actions, useful for demo runs.</p>
     */
    public static Object STEP_DELAY_SECONDS
     

    static {
        try {
            def selectedVariables = TestCaseMain.getGlobalVariables('default')
			selectedVariables += TestCaseMain.getGlobalVariables(RunConfiguration.getExecutionProfile())
    
            FRONTEND_URL = selectedVariables['FRONTEND_URL']
            DEFAULT_TIMEOUT = selectedVariables['DEFAULT_TIMEOUT']
            TEST_PASSWORD = selectedVariables['TEST_PASSWORD']
            STEP_DELAY_SECONDS = selectedVariables['STEP_DELAY_SECONDS']
            
        } catch (Exception e) {
            TestCaseMain.logGlobalVariableError(e)
        }
    }
}
