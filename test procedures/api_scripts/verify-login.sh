source ./tokens.txt

die() {
	echo >&2 "$@"
	exit 1
}

# stop if there aren't the right number of arguments
[ "$#" -eq 1 ] || die "required argument CODE, $# arg(s) provided"
USER_CODE=$1

response=$(curl \
	-H 'Content-Type: application/json' \
	--request POST \
	--data "{\"email\": \"$EMAIL\", \"code\": \"$USER_CODE\"}" \
	http://localhost:8080/session/verify)

# show the response
echo $response | jq

# extract and save the tokens
refresh_token=$(echo $response | jq -r '.refresh_token') && echo "REFRESH=$refresh_token" > tokens.txt
access_token=$(echo $response | jq -r '.access_token') && echo "ACCESS=$access_token" >> tokens.txt
echo "EMAIL=$EMAIL" >> tokens.txt
echo "saved to tokens.txt"
