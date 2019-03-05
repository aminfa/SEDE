/* service_maxrgb.c */

#include "service_maxrgb.h"

typedef int32_t (*run_service_maxrgb_gpu_t)(PixelPacket *, PixelPacket *, int32_t, int32_t, int32_t);

void run_service(char resource, void *handler_gpu, Image **target_images, int32_t *target_images_size, Image *source_images, int32_t source_images_size, double *params, int32_t *status_code) {
	*target_images = (Image *)malloc(sizeof(Image));
	if (NULL == *target_images) {
		fprintf(stderr, "!!! ERROR: Cannot allocate memory.");
		*status_code = -1;
		return;
	}

	(*target_images)[0].rows	= source_images[0].rows;
	(*target_images)[0].columns	= source_images[0].columns;
	*target_images_size			= 1;
	(*target_images)[0].image	= malloc((*target_images)[0].rows * (*target_images)[0].columns * sizeof(PixelPacket));
	if (NULL == (*target_images)[0].image) {
		fprintf(stderr, "!!! ERROR: Cannot allocate memory.");
		*status_code = -1;
		return;
	}

	run_service_maxrgb_gpu_t run_service_maxrgb_gpu;
	char *dlsym_error;

	switch (resource) {
		case 'c':
			*status_code = run_service_maxrgb_cpu((*target_images)[0].image, source_images[0].image, source_images[0].rows, source_images[0].columns, (int32_t) params[0]);
			break;
		case 'g':
			#ifdef PGCC
			handler_gpu = dlopen("../bin/plugins/libservice_maxrgb_gpu.so", RTLD_LAZY | RTLD_GLOBAL);
			if (NULL == handler_gpu) {
				fprintf(stderr, "Cannot open library libservice_maxrgb_gpu.so: %s (file: %s, line: %i)\n", dlerror(), __FILE__, __LINE__);
				exit(EXIT_FAILURE);
			}
			#endif
			run_service_maxrgb_gpu = (run_service_maxrgb_gpu_t) dlsym(handler_gpu, "run_service_maxrgb_gpu");
			dlsym_error = dlerror();
			if (dlsym_error) {
				fprintf(stderr, "Cannot load symbol \"run_service_maxrgb_gpu\": %s (file: %s, line: %i)\n", dlsym_error, __FILE__, __LINE__);
				break;
			}
			*status_code = run_service_maxrgb_gpu((*target_images)[0].image, source_images[0].image, source_images[0].rows, source_images[0].columns, (int32_t) params[0]);
			#ifdef PGCC
			dlclose(handler_gpu);
			#endif
			break;
		case 'f':
			*status_code = run_service_maxrgb_fpga((*target_images)[0].image, source_images[0].image, source_images[0].rows, source_images[0].columns, (int32_t) params[0]);
			break;
		case 's':
		default:
			*status_code = run_service_maxrgb_scpu((*target_images)[0].image, source_images[0].image, source_images[0].rows, source_images[0].columns, (int32_t) params[0]);
			break;
	}
}

void get_service_ids(char ***id_strings, int32_t *id_strings_size) {
	char *strings[] = {
		"MAXRGB",
		"maxrgb"
	};
	*id_strings_size = 2;

	malloc_copy_string_array(id_strings, strings, *id_strings_size);
}

void get_service_info(char ***info_strings, int32_t *info_strings_size) {
	char *strings[] = {
		"Maximum or minimum value of each pixel is increased \n", "mode = 1 is maxRGB.\n", "mode = 2 is minRGB.\n", "mode = 3 is max increase.\n" , "mode = 4 is min erase."
	};
	*info_strings_size = 5;

	malloc_copy_string_array(info_strings, strings, *info_strings_size);
}

void get_service_params(char ***param_strings, int32_t *param_strings_size) {
	char *strings[] = {"mode"};
	*param_strings_size = 1;

	malloc_copy_string_array(param_strings, strings, *param_strings_size);
}

void get_service_resources(char ***resource_strings, int32_t *resource_strings_size) {
	char *strings[] = {
		"CPU", "SCPU", "GPU", "FPGA",
		"cpu", "scpu", "gpu", "fpga",
		"C"  , "S"   , "G"  , "F"   ,
		"c"  , "s"   , "g"  , "f"
	};
	*resource_strings_size = 16;

	malloc_copy_string_array(resource_strings, strings, *resource_strings_size);
}

int32_t get_number_of_input_images(void) {
	return 1;
}

int32_t get_number_of_output_images(void) {
	return 1;
}
