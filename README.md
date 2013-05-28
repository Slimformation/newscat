# NewsCat

A categorizer that uses a ground truth collection of articles about a
certain set of topics and then categorizes new articles based on a
trained model.

The following categories will be considered:
 
- Politics
- Business
- Technology
- Sports
- Science
- Entertainment

To make things quick, we'll just grab some articles from subreddits of
the same name and train a model based on that.

## Developing

I use the excellent `environ`, which is `[environ "0.4.0"]`. Check it
out [here](https://github.com/weavejester/environ). This means you need
`{:user {:env {:readability-parser-api-token "blah"}}}` in your
`~/.lein/profiles.clj`.


## Deploying

If deploying to Heroku, you need to `heroku config:set
READABILITY_PARSER_API_TOKEN=blah`.

