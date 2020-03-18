#!/usr/bin

WORK_DIR=$(dirname "$0")

if [[ `uname` == 'Darwin' ]]; then
    sed -i "" "s#^USE_ALIYUN_REPO=.*#USE_ALIYUN_REPO=$1#g" "$WORK_DIR/../gradle.properties"
fi

if [[ `uname` == 'Linux' ]]; then
    sed -i "s#^USE_ALIYUN_REPO=.*#USE_ALIYUN_REPO=$1#g" "$WORK_DIR/../gradle.properties"
fi
