import React from 'react';
import { Menu } from 'react-feather';
import { Link } from 'react-router-dom';

function Navigation(props) {
  if (!props.visible) {
    return(
      <div></div>
    );
  } else {
    return (
      <div className="header-modal">
        <Menu className="icon" onClick={props.backClick} />
        <h1>Menu</h1>
        {props.links.map(link =>
          <Link to={link.nav} onClick={props.backClick}>
            <h3>{link.display}</h3>
          </Link>
        )}
      </div>
    );
  }
}

class Header extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      menuVisible: false,
      navigationLinks: props.links,
    };
  }

  toggleMenuVisibility() {
    const shouldShow = !this.state.menuVisible;
    this.setState({ menuVisible: shouldShow });
  }

  render() {
    return (
      <div>
        <div className="header">
          <div className="header-content">
            <div className="header-grid">
              <Menu className="icon" onClick={() => this.toggleMenuVisibility()} />
              <div>McClain Cookbook</div>
            </div>
          </div>
        </div>
        <Navigation
          visible={this.state.menuVisible}
          links={this.state.navigationLinks}
          backClick={() => this.toggleMenuVisibility()}
        />
      </div>
    );
  }
}

export default Header;
