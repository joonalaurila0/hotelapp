#!/bin/sh
# Generates a join token for the swarm.

tokenfile="token.txt"
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

query() {
  prompt=$1
  echo -n "$1"
  while :
  do
    read input_string
    case $input_string in
      Y|y|Yes|yes|"")
				rm -vf token.txt
        docker swarm join-token worker | grep 'docker swarm join' > token.txt && echo "New token generated at $dir/$tokenfile"
        break
        ;;
      N|n|No|no)
        docker swarm join-token worker | grep 'docker swarm join' > token.txt && echo "New token generated at $dir/$tokenfile"
        break
        ;;
      *)
        echo "Please pick yes or no!"
        ;;
    esac
  done
}

if [ -f $tokenfile ]; then
  query "token.txt already exists, would you like to delete it? [Y]es/[N]o? "
else
  docker swarm join-token worker | grep 'docker swarm join' > token.txt && echo "New token generated at $dir/$tokenfile"
fi
