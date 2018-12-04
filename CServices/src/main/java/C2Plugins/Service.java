package C2Plugins;

import C2Data.C2Params;

import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class Service {
	File serviceFile;
	List<File> linkedFiles;
	ServiceMetaInformation metaInfos = null;
	C2Params mParams;

	public Service(File serviceFile, List<File> linkedFiles) {
		this.serviceFile = serviceFile;
		this.linkedFiles = linkedFiles;
	}

	public void setMetaInfos(ServiceMetaInformation metaInfos) {
		this.metaInfos = metaInfos;
	}

	public ServiceMetaInformation getMetaInfosCopy() {
		return metaInfos.deepCopy();
	}

	public ServiceMetaInformation getMetaInfos() {
		return metaInfos;
	}

	public void setOptions(C2Params params) {
		mParams = params;
	}

	public C2Params getOptions() {
		return mParams;
	}
}
