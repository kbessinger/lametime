'use strict';

const React = require('react');
const ReactDOM = require('react-dom');
const stompClient = require('./websocket');

import { Button } from 'react-bootstrap';
import { FormControl } from 'react-bootstrap';
import { Panel } from 'react-bootstrap';
import { FormGroup } from 'react-bootstrap';
import { InputGroup } from 'react-bootstrap';
import { Navbar } from 'react-bootstrap';



class App extends React.Component {

    constructor(props) {
        super(props);
        var msgs;
        if(localStorage.getItem("lametime-messages") === null || localStorage.getItem("lametime-messages") == "null") {
			msgs = JSON.parse('[]');
        } else {
        	msgs = JSON.parse(localStorage.getItem('lametime-messages'));
        }
        this.state = {stateInfo : [], messages: msgs}
        this.state = {stateInfo : [], messages: msgs}
        this.stompClient = {};
        this.handleMessage = this.handleMessage.bind(this);
        this.handleState = this.handleState.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
        this.clearMessages = this.clearMessages.bind(this);
    }


    handleMessage(msg) {
        var chatMessage = JSON.parse(msg.body);
        var msgs = this.state.messages.slice();
        msgs.push(chatMessage);
        this.setState({
            stateInfo: this.state.stateInfo,
            messages: msgs
        })
        if(msg !== null) {
            localStorage.setItem('lametime-messages', JSON.stringify(msgs));
        }
    }

    handleState(msg) {
        var stateInfo = JSON.parse(msg.body);
        this.setState({
            stateInfo: stateInfo
        });
        console.log(stateInfo);
    }

	componentDidMount() {
	    this.stompClient = stompClient;
		stompClient.register([
			{route: '/topic/messages', callback: this.handleMessage},
			{route: '/topic/state', callback: this.handleState}
		]);
	}

	sendMessage(message) {
        this.stompClient.send(message);
    }

    clearMessages() {
        this.setState({
            messages: []
        });
        localStorage.setItem('lametime-messages', '[]');
    }

	render() {
	    return (
	            <div>
	                <LametimeHeader/>
	                <div className="container-fluid">
	                    <div className="row">
	                        <div className="col-lg-12">
	                            <LametimeSendMessage sendMessage={this.sendMessage}/>
	                        </div>
	                    </div>
	                    <div className="fill-80 row">
	                        <div className="fill col-sm-12">
	                            <div className="lametime-main row">
                                    <div className="lametime-userlist col-sm-3">
                                        <LametimeUserList attributes={this.state.stateInfo}/>
                                    </div>
                                    <div className="lametime-conversation col-sm-9">
                                        <LametimeConversation messages={this.state.messages}/>
                                    </div>
                                </div>
                            </div>
	                     </div>
	                     <div className="row">
	                        <div className="col-sm-12">
	                            <LametimeActionBar clearMessages={this.clearMessages}/>
	                        </div>
	                    </div>
	                </div>
	            </div>
	    )
	}
}

class LametimeHeader extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
                <Navbar>
                    <Navbar.Header>
                      <Navbar.Brand>
                        <a href="#">Lametime</a>
                      </Navbar.Brand>
                    </Navbar.Header>
                </Navbar>
        )
    }
}

class LametimeSendMessage extends React.Component {
    constructor(props) {
        super(props);
        this.state = { input: '' };
        this.handleChange = this.handleChange.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
        this.handleKeyPress = this.handleKeyPress.bind(this);
    }

    sendMessage() {
        if(this.state.input.length > 0) {
            this.props.sendMessage(this.state.input);
        }
        this.setState({
            input: ''
        })
        ReactDOM.findDOMNode(this.refs.textEntry).focus();
    }

    handleChange(e) {
        this.setState({
            input: e.target.value
        });
    }

    handleKeyPress(e) {
        if(e.key == 'Enter') {
            this.sendMessage();
        }
    }

    render() {
        return (
                <FormGroup>
                    <InputGroup>
                        <FormControl autoFocus={true} type="text" id="textEntry" ref="textEntry" placeholder="Send a message..." onChange={ this.handleChange } onKeyPress={this.handleKeyPress} value={this.state.input}/>
                        <InputGroup.Button>
                            <Button bsStyle="primary" id="sendMessage" onClick={this.sendMessage}>Send</Button>
                        </InputGroup.Button>
                    </InputGroup>
                </FormGroup>
        )
    }
}

class LametimeConversation extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {

        var messages = [];

        if(this.props.messages !== undefined) {
            messages = this.props.messages.map((message) => <LametimeIncomingMessage key={message.id} message={message}/>);
        }

        return (
                <Panel className="fill" id="conversationDiv">
                    {messages}
                </Panel>
        )
    }
}

class LametimeIncomingMessage extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        //do this for now?
        var d = new Date();

        return (
            <p className='message'><span className='from'>{this.props.message.user.name}: </span>{this.props.message.text + ' (' + d.toLocaleString()/*this.props.message.time*/ + ')'}</p>
        )
    }
}

class LametimeActionBar extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
                <div id="clearDiv"><LametimeClearMessages clearMessages={this.props.clearMessages}/></div>
        )
    }
}

class LametimeClearMessages extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
                <Button bsStyle="primary" id="clear" onClick={this.props.clearMessages}>Clear</Button>
        )
    }
}

class LametimeUserList extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        var users = [];

        if(this.props.attributes.connectionInfo !== undefined) {
            users = this.props.attributes.connectionInfo.users.map((user) => <LametimeUser key={user.uuid} user={user}/>);
        }

        const title = (<h3>Users</h3>);

        return (
                <Panel className="fill" id="userList" header={title}>
                    {users}
                </Panel>
        )
    }

}

class LametimeUser extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
                <div className="row">
                    <div className="col-xs-12">
                        <img className='lametime-user-img img-circle' width='32' src={this.props.user.picture}/>
                        {this.props.user.name}
                    </div>
                </div>
        )
    }
}

ReactDOM.render(
    <App/>,
    document.getElementById('root')
);