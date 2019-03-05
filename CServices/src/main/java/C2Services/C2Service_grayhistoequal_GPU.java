package C2Services;

import C2Data.C2Image;
import C2Data.C2Params;
import C2Data.C2Resource;

import java.util.Map;
import java.util.Set;

public class C2Service_grayhistoequal_GPU extends C2Service_grayhistoequal{
	private C2Resource mResource;
	public C2Service_grayhistoequal_GPU() {
		super();

		mResource = new C2Resource("gpu");
	}

	public C2Image processImage(C2Image sourceImage) {
		return super.processImage(mResource, sourceImage);
	}
}
