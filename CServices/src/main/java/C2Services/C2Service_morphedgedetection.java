package C2Services;


import C2Data.C2Image;
import C2Data.C2NativeInterface;
import C2Data.C2Params;
import C2Data.C2Resource;
import C2Plugins.Plugin;

import java.io.File;
import java.util.*;

public class C2Service_morphedgedetection extends Plugin {

    private static File serviceFile;
    private static List<File> linkedFiles;

    static {
        serviceFile = new File(C2NativeInterface.getPluginDir() + "libservice_morphedgedetection.so");
        linkedFiles = new ArrayList<File>();
        linkedFiles.add(new File(C2NativeInterface.getPluginDir() + "libservice_morphedgedetection_cpu.so"));
        linkedFiles.add(new File(C2NativeInterface.getPluginDir() + "libservice_morphedgedetection_scpu.so"));
        //linkedFiles.add(new File(C2NativeInterface.getPluginDir() + "libservice_morphedgedetection_gpu.so"));
        linkedFiles.add(new File(C2NativeInterface.getPluginDir() + "libservice_morphedgedetection_fpga.so"));
        linkedFiles.add(new File(C2NativeInterface.getPluginDir() + "libservice_morphedgedetection_overlay.so"));
    }

    public C2Service_morphedgedetection() {
        super(serviceFile, linkedFiles);

        Map<String, Double> paramsDefaultMap = new HashMap<String, Double>();
        paramsDefaultMap.put("matrix_size", 3.0);
        setOptions(new C2Params(paramsDefaultMap));

        printMethod("__construct");
    }

    protected List<Double> getParamList() {
        List<Double> paramList      = new ArrayList<Double>();
        Map<String,Double> paramMap = getOptions().getParams();

        paramList.add(paramMap.get("matrix_size"));

        return paramList;
    }

    public C2Image processImage(C2Resource resource, C2Image sourceImage) {
        printMethod("processImage", resource.getResourceString());

        switch (resource.getResourceString()) {
            case "s":
            case "c":
            case "g":
            case "f":
                break;
            case "j":
            default:
                throw new Error("Resource '" + resource.getResourceString() + "' not supported by service.");
        }

        List<C2Image> input_images  = new ArrayList<C2Image>();
        input_images.add(sourceImage);

        List<C2Image> output_images = null;

        output_images = (ArrayList<C2Image>) process(resource.getResourceChar(), getParamList(), input_images);

        return output_images.get(0);
    }
}
