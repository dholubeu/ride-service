#!/bin/bash
function prop {
    grep -w "${1}" .env|cut -d'=' -f2
}

declare -a index_names=("promocodes")
declare -a mapping_files=("elasticsearch/mapping-v1.json")

for i in ${!index_names[@]}; do
    index_name=${index_names[$i]}
    mapping_file=${mapping_files[$i]}

    echo "Checking index: ${index_name}"
    echo "Using mapping file: ${mapping_file}"

    # Check if the index exists
    response=$(curl -s -o /dev/null -w "%{http_code}" $(prop 'ES_HOST'):9200/${index_name})
    echo "Response: ${response}"

    if [ "$response" -eq "404" ]; then
        # If the index does not exist, create it
        echo "Creating index: ${index_name}"
        curl -X PUT localhost:9200/${index_name} -H 'Content-Type: application/json' -d @${mapping_file}
    else
        echo "Index ${index_name} already exists"
    fi
done