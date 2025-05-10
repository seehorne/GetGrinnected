source ./tokens.txt

die() {
	echo >&2 "$@"
	exit 1
}

# stop if there aren't the right number of args
[ "$#" -eq 2 ] || die "required arguments CODE NEW_EMAIL, $# arg(s) provided"
USER_CODE=$1
NEW_EMAIL=$2

response=$(curl \
	-H 'Content-Type: application/json' \
	--request POST \
	--data "{\"email\": \"$NEW_EMAIL\", \"code\": \"$USER_CODE\"}" \
	http://localhost:8080/session/verify)

# show the response
echo $response | jq

# extract and save the tokens
refresh_token=$(echo $response | jq -r '.refresh_token') && echo "REFRESH=$refresh_token" > tokens.txt
access_token=$(echo $response | jq -r '.access_token') && echo "ACCESS=$access_token" >> tokens.txt
echo "EMAIL=$NEW_EMAIL" >> tokens.txt
echo "saved to tokens.txt"
