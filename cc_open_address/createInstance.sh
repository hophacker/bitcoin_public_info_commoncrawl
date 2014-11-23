#!/bin/bash - 
#ubuntu
#instanceType=ami-76817c1e
#Amazon Linux AMI 2014.03.2 (HVM) -                 ami-76817c1e
#Ubuntu Server 14.04 LTS (HVM), SSD Volume Type -   ami-864d84ee

#c3.large    273.75      2 x 16SSD       $0.105/Hour
imageID="ami-864d84ee"
ami-e7b8c0d7
instanceType="c3.large"
number=1

#ec2-run-instances $machineType -n $number --instance-type instanceType -k hadoop  --region us-east-1
ec2-run-instances $imageID -n $number --instance-type $instanceType -k Oregon  --region us-west-1

#aws ec2 run-instances --image-id ami-a7fdfee2 --count 1 --instance-type t2.medium --key-name BTC
