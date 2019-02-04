#!/bin/bash

sudo docker rm sfbhetnode_$USER
sudo docker run \
	-v $PWD:/root/script \
	-v $PWD/../../../SEDE:/root/SEDE \
	-v /opt/pgi:/opt/pgi \
	-v /usr/local/cuda-8.0:/usr/local/cuda-8.0 \
	-v /usr/lib64/libnuma.so.1:/usr/lib64/libnuma.so.1 \
        -v /usr/lib64/nvidia:/usr/lib64/nvidia \
        -v /usr/bin/nvidia-smi:/usr/bin/nvidia-smi \
        -v /opt/maxeler/maxeleros:/opt/maxeler/maxeleros \
	-p 9000:9000 \
	--device /dev/nvidia0 \
	--device /dev/nvidiactl \
	--device /dev/nvidia-uvm \
	--device /dev/nvidia-uvm-tools \
        --device /dev/nvram \
	--device /dev/maxeler0 \
	--name sfbhetnode_$USER \
	-idt centos:centos7 \
	/root/script/script.sh