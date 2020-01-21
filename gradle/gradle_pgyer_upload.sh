# !/bin/bash

FILEPATH=$1
API_KEY=$2

function panic()
{
    local exitCode=$1
    set +e

    shift
    [[ "$@" == "" ]] || \
        echo "$@" >&2

    exit $exitCode
}

if [ ! -n "$FILEPATH" -o ! -n "$API_KEY" ]; then
	panic 1 "Usage: ./pgyer_upload.sh <FilePath> <APIKey>"
fi

if [ ! -f "$FILEPATH" ]; then
	panic 1 "PGYER: can not find file, pelase check yout file path"
fi

result=$(curl -S "http://www.pgyer.com/apiv2/app/upload" -F "file=@${FILEPATH}" -F "_api_key=${API_KEY}")

code=$(echo $result | awk -F ':' '{print $2}' | awk -F ',' '{print $1}')
message=$(echo $result | awk -F ':' '{print $3}' | awk -F '"' '{print $2}')

if [ $code -eq 0 ]; then
    panic 0 "PGYER: upload app package to PGYER success"
else
	panic 1 "PGYER: upload app package to PGYER failed! Error message: ${message}"
fi
