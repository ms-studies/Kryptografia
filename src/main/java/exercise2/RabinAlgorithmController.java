package exercise2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by szale_000 on 2017-03-04.
 */
public class RabinAlgorithmController {
    private static final Integer PRIVATE_KEY_P = 19;
    private static final Integer PRIVATE_KEY_Q = 23;
    private static final Integer PUBLIC_KEY_N = PRIVATE_KEY_P * PRIVATE_KEY_Q;
    @FXML
    TextArea plainTextTextarea;
    public Button buttonEncrypt;
    @FXML
    private TextArea privateKeyPTextarea;
    @FXML
    private TextArea privateKeyQTextarea;
    @FXML
    private TextArea publicKeyTextarea;
    @FXML
    private TextArea plainTextIntegerTextarea;
    @FXML
    private TextArea encryptedTextTextarea;
    @FXML
    private TextArea decryptedTextTextarea;


    private PythonInterpreter interpreter = new PythonInterpreter();

    public RabinAlgorithmController() {

    }

    @FXML
    public void handleEncryptClick() {
        final String textToEncrypt = plainTextTextarea.getText();
        interpreter.execfile("rabin.py");
        calculateTextToInteger();
        findPrivateKey();
        findPublicKey();
        encrypt();
    }

    @FXML
    public void handleDecryptClick() {
        interpreter.execfile("rabin.py");
        PyObject decryptMessage = interpreter.get("decrypt");
        PyObject decryptedMessageResult = decryptMessage.__call__(
                new PyString(privateKeyPTextarea.getText()),
                new PyString(privateKeyQTextarea.getText()),
                new PyString(encryptedTextTextarea.getText())
        );
        List<String> decryptedMessage = (List) decryptedMessageResult.__tojava__(List.class);
        decryptedTextTextarea.setText(Arrays.toString(decryptedMessage.toArray()));
    }

    private void calculateTextToInteger() {
        PyObject integerFromText = interpreter.get("getIntegerFromText");
        PyObject integerFromTextResult = integerFromText.__call__(
                new PyString(plainTextTextarea.getText())
        );

        plainTextIntegerTextarea.setText((String) integerFromTextResult.__tojava__(String.class));
    }

    private void findPrivateKey() {
        PyObject findPrivateKey = interpreter.get("findPrivateKey");
        PyObject privateKeyResult = findPrivateKey.__call__();
        List<String> privateKey = (List) privateKeyResult.__tojava__(List.class);

        privateKeyPTextarea.setText(privateKey.get(0));
        privateKeyQTextarea.setText(privateKey.get(1));
    }

    private void findPublicKey() {
        PyObject findPublicKey = interpreter.get("findPublicKey");
        PyObject publicKeyResult = findPublicKey.__call__(
                new PyString(privateKeyPTextarea.getText()),
                new PyString(privateKeyQTextarea.getText())
        );

        publicKeyTextarea.setText((String) publicKeyResult.__tojava__(String.class));
    }

    private void encrypt() {
        PyObject encryptMessage = interpreter.get("encrypt");
        PyObject encryptedMessageResult = encryptMessage.__call__(
                new PyString(plainTextTextarea.getText()),
                new PyString(publicKeyTextarea.getText())
        );

        encryptedTextTextarea.setText((String) encryptedMessageResult.__tojava__(String.class));
    }
}
