import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

// converting PCM unsigned mono 8-bit 20KHz to 8-levels volume PWM test

// formulas:
// human max audible frequency = 20000hz
// pwm_player_sample_rate / 20000 = 8 levels of volume
// pwm_player_sample_rate = 160000 hz
public class App {
    public static void main(String[] args) throws Exception {
        try {
            AudioFormat audioFormat = new AudioFormat(160000, 8, 1, false, false);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getSourceDataLine(audioFormat);

            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            AudioInputStream ais = AudioSystem.getAudioInputStream(App.class.getResourceAsStream("music.wav"));
             
            byte[] musicData = ais.readAllBytes();
            

            byte[][] test = {  
                {  0,   0,   0,   0,   0,   0,   0,   0 },
                {  0,   0,   0,   0,   0,   0,   0,  64 },
                {  0,   0,   0,   0,   0,   0,  64,  64 },
                {  0,   0,   0,   0,   0,  64,  64,  64 },
                {  0,   0,   0,   0,  64,  64,  64,  64 },
                {  0,   0,   0,  64,  64,  64,  64,  64 },
                {  0,   0,  64,  64,  64,  64,  64,  64 },
                {  0,  64,  64,  64,  64,  64,  64,  64 },
            }; 

            for (int i = 0; i < musicData.length;i++) {
                int b = musicData[i + 1] & 0xff;
                int sample = b >> 5; // convert to 3-bit (8 levels of volume)
                sourceDataLine.write(test[sample], 0, test[sample].length);
                
            }

            sourceDataLine.drain();
            sourceDataLine.close();
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
