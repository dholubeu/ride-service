function prop {
    grep -w "${1}" .env|cut -d'=' -f2
}

mapping_file=${mapping_file:-elasticsearch/mapping-v2.json}
curl -s -H "Content-Type: application/x-ndjson" -XPOST $(prop 'ES_HOST'):9200/_bulk --data-binary @${mapping_file}