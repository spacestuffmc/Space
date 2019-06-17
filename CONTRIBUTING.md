Contributing to **Space**
================================

As general housekeeping guidelines, we ask that you follow some of these basic guidelines and conventions for contributing code to Space. If you do not follow these guidelines, there may be a delay in your pull request being merged, or possibly rejected.

## Commiting

* Make sure your commits do not include any arbitrary files (e.g. IDE-specific files)
* Keep commits lean and few - keep them to a minimum and squash commits if needed (see: `git rebase -i`)

## Formatting

* Limit lines to 120 characters before they start a new line (most IDEs have settings for this)
* JavaDocs are expected for major contributions
 * If you think it might need a comment, you're right
* Organize imports alphabetically

## Credit

* If contributing more than a "simple fix", add your name to the authors list in `plugin.yml`
* If you make major changes to a class, add an `@author` JavaDocs tag

## Testing

* Pull requests should be tested before submission
 * "Does it compile?"
 * "Does the plugin run on the latest Spigot server?"

Follow the above conventions if you want your pull requests accepted.