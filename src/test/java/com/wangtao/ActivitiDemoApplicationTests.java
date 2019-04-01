package com.wangtao;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.sql.DataSource;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * @author : want
 * @Team Home
 * @date : 2019-3-4 14:01 星期一
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDemoApplicationTests {

    @Resource(name = "myDataSource")
    private DataSource myDataSource;

    @Test
    public void init() {
        ProcessEngineConfiguration configuration = SpringProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        configuration.setDataSource(myDataSource);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        String name = processEngine.getName();
        System.out.println("name = " + name);
    }

    @Test
    public void qrCode() throws WriterException, IOException {
        String qrcodeFilePath = "";
        int qrcodeWidth = 300;
        int qrcodeHeight = 300;
        String qrcodeFormat = "png";
        HashMap<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode("http://www.cnblogs.com/java-class/", BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);

        BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_RGB);
        Random random = new Random();
        File QrcodeFile = new File("C:\\Users\\want\\Desktop\\test\\" + random.nextInt() + "." + qrcodeFormat);
        ImageIO.write(image, qrcodeFormat, QrcodeFile);
        MatrixToImageWriter.writeToFile(bitMatrix, qrcodeFormat, QrcodeFile);
        qrcodeFilePath = QrcodeFile.getAbsolutePath();

    }

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Test
    public void testFastDfsUpload() throws FileNotFoundException {
        //
        File file = new File("C:\\Users\\want\\Desktop\\images\\home.png");
        FileInputStream fileInputStream = new FileInputStream(file);

        //StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet);
        Set<MetaData> metaDataSet = Sets.newHashSet();
        MetaData name = new MetaData("name", file.getName());
        MetaData size = new MetaData("size", String.valueOf(file.length()));
        metaDataSet.add(name);
        metaDataSet.add(size);
        StorePath storePath = fastFileStorageClient.uploadFile(fileInputStream, file.length(), "png", metaDataSet);

        String fullPath = storePath.getFullPath();
        System.out.println("fullPath = " + fullPath);
        String path = storePath.getPath();
        System.out.println("path = " + path);
        String group = storePath.getGroup();
        System.out.println("group = " + group);

    }
}
