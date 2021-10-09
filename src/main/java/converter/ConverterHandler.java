package converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class ConverterHandler implements RequestHandler<Map<String, String>, String> {
    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            LambdaLogger logger = context.getLogger();
            String svgData = input.get("data");

            logger.log("svgData = " + svgData);

            ByteArrayInputStream svgInput = new ByteArrayInputStream(svgData.getBytes());
            TranscoderInput transcoderInput = new TranscoderInput(svgInput);

            TranscoderOutput transcoderOutput = new TranscoderOutput(byteArrayOutputStream);

            PNGTranscoder pngTranscoder = new PNGTranscoder();
            pngTranscoder.transcode(transcoderInput, transcoderOutput);

            return byteArrayOutputStream.toString();
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Failed To Convert");
        }
    }
}
