<h2 align="center">
  <img src="https://github.com/pavitra-singh/SyntHIR-SMART-app/blob/development/public/SyntHIR_logo.PNG" height="150px">
</h2>

<h4 align="center">
    Accelerating translation from clinical research to tools
</h4>

## Synthetic Data Generator

This component generates synthetic data using Gretel CLI of Gretel synthetics. For running the component, first install gretel on a cloud server. Follow the below steps to configure gretel:

1. Register on Gretel on this link: https://console.gretel.ai/login/

![](README_images/gretel_login.png "a title")

To configure the following details to the properties file (application-prod.properties) in resources folder:

1. gretel.server.model.path=~/.gretel/config-xxxx-xxxx.yml
2. gretel.project.name=project-name
3. gretel.server.host.name=xxxx.uit.no
4. gretel.server.user.name=usxx
5. gretel.server.password=passxxx
6. gretel.server.openssh.private.key.path=path/to/openssh/key
7. gretel.server.real.data.input.directory.path=/path/to/real/input/data
8. gretel.server.synthetic.data.output.directory.path=/path/to/synthetic/output/data
9. server.port=XXXX (Port on which the application will run)
