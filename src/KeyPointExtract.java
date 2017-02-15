import java.io.File;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class KeyPointExtract {
  private static String extractFileName = "./result.txt";
  private final static int FILENUM = 12;
  private static File extractFile;
  private static PrintWriter writer;

  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Please input path.");
      return;
    }
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    try {
      extractFile = new File(extractFileName);
      writer = new PrintWriter(new BufferedWriter(new FileWriter(extractFile)));
    } catch (Exception e) {
      e.printStackTrace();
    }

    String name = args[0];
    for (int i = 0; i < args.length; i++) {
      extract(String.format("%s", args[i]));
      writer.println();
    }

    writer.close();
  }


  public static void extract(String path) {
    FeatureDetector detector = FeatureDetector.create(FeatureDetector.DENSE);
    DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);

    Mat img = Highgui.imread(path);
    Mat grayImg = new Mat(img.rows(), img.cols(), img.type());
    Imgproc.cvtColor(img, grayImg, Imgproc.COLOR_BGR2GRAY);

    MatOfKeyPoint keyPoint = new MatOfKeyPoint();
    detector.detect(grayImg, keyPoint);

    KeyPoint[] points = keyPoint.toArray();
    for (KeyPoint p: points) {
      writer.println(String.format("%f, %f, %f, %f, %f, %d, %d", p.pt.x, p.pt.y, p.size, p.angle, p.response, p.octave, p.class_id));
    }
  }
}
